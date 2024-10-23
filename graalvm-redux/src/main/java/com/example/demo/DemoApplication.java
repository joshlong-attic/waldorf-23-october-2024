package com.example.demo;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@ImportRuntimeHints(Hints.class)
@RegisterReflectionForBinding(Dog.class)
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}


class BFIAP implements BeanFactoryInitializationAotProcessor {

    @Override
    public BeanFactoryInitializationAotContribution processAheadOfTime(
            ConfigurableListableBeanFactory beanFactory) {

        for (var bd : beanFactory.getBeanDefinitionNames())
            System.out.println(" bd: " + bd);

        return (gc, code) -> {
            RuntimeHints runtimeHints = gc.getRuntimeHints();
            runtimeHints.reflection().registerType(Foo.class);
            code.getMethods().add("hello", builder -> {
               builder.addCode("""
                       System.out.println("hello, world");
                       """) ;
            });
        };
    }
}

class Hints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(Foo.class , MemberCategory.values());
//        hints.resources().registerResource(new ClassPathResource("mydogs.json"));
    }
}

record Dog(int id) {
}

class BPP implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        class MyFoo extends Foo {

            MyFoo() {
                System.out.println("MyFoo");
            }
        }

        System.out.println("bean " + beanName + " : " + bean + "");
        if (bean instanceof Foo) {
            return new MyFoo();
        }
        return bean;
    }

}

class MyBFPP implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        for (String beanName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition bd = beanFactory.getBeanDefinition(beanName);
            System.out.println("bfpp: " + beanName + " : " + bd.getBeanClassName());
        }

    }
}

@Configuration
class MyConfig {

    @Bean
    static BFIAP bfiap() {
        return new BFIAP();
    }

    @Bean
    static BPP bpp() {
        return new BPP();
    }

    @Bean
    static MyBFPP myBFPP() {
        return new MyBFPP();
    }

    @Bean
    Foo foo() {
        return new Foo();
    }

}

@Component
class Bar {

    final Foo foo;

    Bar(Foo foo) {
        this.foo = foo;
    }
}

class Foo {
}