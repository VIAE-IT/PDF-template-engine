package com.viae.common.pdf.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import com.viae.common.pojo.PojoBuilder;


public class PojoTester {

    private static final Random RANDOM = new Random();

    public static <POJO> void test(final Class<POJO> classToTest){
        test(classToTest, new LinkedList<String>());
    }

    public static <POJO> void test(final Class<POJO> classToTest, final List<String> fieldsToIgnore){
        try{
            builderShouldHaveLazyInit(classToTest);
            cleanShouldBeCalledWhenRequestingBuilder(classToTest);
            settersShouldBePrivate(classToTest, fieldsToIgnore);
            settingAndGettingOfFieldsShouldBeOk(classToTest, fieldsToIgnore);
            deepCopyOfAFieldShouldBeOk(classToTest, fieldsToIgnore);
            defaultMethodsShouldBeOk(classToTest, fieldsToIgnore);
            equalsTest(classToTest, fieldsToIgnore);
        } catch(final Throwable e){
            throw new RuntimeException(e);
        }
    }

    private static <POJO> void equalsTest(final Class<POJO> classToTest, final List<String> fieldsToIgnore) throws Throwable {
        final BiConsumer<String, Field> fieldConsumer = new BiConsumer<String, Field>() {
            @Override
            public void accept(String fieldName, Field field) {
            try{
                final PojoBuilder<POJO> builderInstance = callBuilder(classToTest);
                builderInstance.create();
                final Method builderMethod = builderInstance.getClass().getDeclaredMethod(fieldName, field.getType());

                final Object dummyValue1 = getRandomValue(field.getType());
                builderMethod.invoke(builderInstance, dummyValue1);
                final POJO pojoInstance1 = builderInstance.build();

                builderInstance.create();
                final Object dummyValue2 = getRandomValue(field.getType());
                builderMethod.invoke(builderInstance, dummyValue2);
                final POJO pojoInstance2 = builderInstance.build();

                assertFalse(dummyValue1.equals(dummyValue2));
                assertEquals(pojoInstance1, pojoInstance1);
                assertFalse(pojoInstance1.equals(null));
                assertFalse(pojoInstance1.equals(pojoInstance2));
            } catch(final Throwable e){
                throw new RuntimeException(e);
            }
        }
        };
        doForField(classToTest, fieldsToIgnore, fieldConsumer);
    }

    private static <POJO> void defaultMethodsShouldBeOk(final Class<POJO> classToTest, final List<String> fieldsToIgnore) throws Throwable {
        final BiConsumer<String, Field> fieldConsumer = new BiConsumer<String, Field>() {
            @Override
            public void accept(String fieldName, Field field) {
            try{
                final PojoBuilder<POJO> builderInstance = callBuilder(classToTest);
                builderInstance.create();
                final Method builderMethod = builderInstance.getClass().getDeclaredMethod(fieldName, field.getType());
                final Object dummyValue = getRandomValue(field.getType());
                builderMethod.invoke(builderInstance, dummyValue);

                final POJO pojoInstance = builderInstance.build();
                assertTrue("toString: " + pojoInstance.toString() + "doesn't contain field name: " + fieldName, pojoInstance.toString().contains(fieldName));
                assertTrue("toString: " + pojoInstance.toString() + "doesn't contain field value: " + dummyValue, pojoInstance.toString().contains(String.valueOf(dummyValue)));
                assertTrue(pojoInstance.hashCode() != 0);
            } catch(final Throwable e){
                throw new RuntimeException(e);
            }
            }
        };
        doForField(classToTest, fieldsToIgnore, fieldConsumer);
    }

    private static <POJO> void deepCopyOfAFieldShouldBeOk(final Class<POJO> classToTest, final List<String> fieldsToIgnore) throws Throwable {
        final BiConsumer<String, Field> fieldConsumer = new BiConsumer<String, Field>() {
            @Override
            public void accept(String fieldName, Field field) {
            try{
                final PojoBuilder<POJO> builder = callBuilder(classToTest);
                builder.create();
                final Class<?> type = field.getType();
                final Method builderMethod = builder.getClass().getDeclaredMethod(fieldName, type);
                final String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                final Method getter = classToTest.getDeclaredMethod(getterName);
                final Object dummyValue = getRandomValue(type);

                builderMethod.invoke(builder, dummyValue);
                final POJO pojoInstance = builder.build();

                final Object result = getter.invoke(builder.deepCopy(pojoInstance).build());
                assertEquals("Test deep copy for field: " + fieldName, dummyValue, result);
                assertEquals("Test deep copy for field after null copy: " + fieldName, getDefaultValue(type), getter.invoke(builder.deepCopy(null).build()));
            } catch(final Throwable e){
                throw new RuntimeException(e);
            }
            }
        };
        doForField(classToTest, fieldsToIgnore, fieldConsumer);
    }

    private static <POJO> void settingAndGettingOfFieldsShouldBeOk(final Class<POJO> classToTest, final List<String> fieldsToIgnore) throws Throwable {
        final BiConsumer<String, Field> fieldConsumer = new BiConsumer<String, Field>() {
            @Override
            public void accept(String fieldName, Field field) {
            try{
                final PojoBuilder<POJO> builderInstance = callBuilder(classToTest);
                builderInstance.create();
                final Method builderMethod = builderInstance.getClass().getDeclaredMethod(fieldName, field.getType());
                final String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                final Method getter = classToTest.getDeclaredMethod(getterName);
                final Object dummyValue = getRandomValue(field.getType());
                builderMethod.invoke(builderInstance, dummyValue);
                final Object result = getter.invoke(builderInstance.build());
                assertEquals("Test getting and setting for field: " + fieldName, dummyValue, result);
            } catch(final Throwable e){
                throw new RuntimeException(e);
            }
            }
        };
        doForField(classToTest, fieldsToIgnore, fieldConsumer);
    }

    private static <TYPE> Object getRandomValue(final Class<TYPE> type) {
        if(double.class.getSimpleName().equals(type.getSimpleName())){
            return RANDOM.nextDouble();
        } else {
            throw new UnsupportedOperationException(type + " is not yet supported");
        }
    }

    private static <TYPE> Object getDefaultValue(final Class<TYPE> type) {
        if(double.class.getSimpleName().equals(type.getSimpleName())){
            return 0.0;
        } else {
            throw new UnsupportedOperationException(type + " is not yet supported");
        }
    }

    private static <POJO> void settersShouldBePrivate(final Class<POJO> classToTest, final List<String> fieldsToIgnore) throws Throwable {
        final BiConsumer<String, Field> fieldConsumer = new BiConsumer<String, Field>() {
            @Override
            public void accept(String fieldName, Field field) {
            try{
                final String setterName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                final Method setter = classToTest.getDeclaredMethod(setterName, field.getType());
                assertTrue(classToTest.getSimpleName() + "." + setterName + " is not private", Modifier.isPrivate(setter.getModifiers()));
            } catch(final Throwable e){
                throw new RuntimeException(e);
            }
            }
        };
        doForField(classToTest, fieldsToIgnore, fieldConsumer);
    }

    private static <POJO> void doForField(final Class<POJO> classToTest, final List<String> fieldsToIgnore, final BiConsumer<String, Field> fieldConsumer) {
        final Field[] fields = classToTest.getDeclaredFields();

        for(final Field field : fields){
            final String fieldName = field.getName();
            if(!fieldName.startsWith("$") && !fieldsToIgnore.contains(fieldName)){
                fieldConsumer.accept(fieldName, field);
            }
        }
    }

    private static <POJO> void builderShouldHaveLazyInit(final Class<POJO> classToTest) throws Throwable {
        final Class<?> forName = Class.forName(classToTest.getCanonicalName() + "$Builder$LazyInit");
        assertNotNull(forName);
    }

    private static <POJO> void cleanShouldBeCalledWhenRequestingBuilder(final Class<POJO> classToTest) throws Throwable {
        final PojoBuilder<POJO> builder = callBuilder(classToTest);
        assertNull(builder.build());
        assertNull(builder.create().clean().build());
        assertNotNull(builder.create().build());
        final PojoBuilder<POJO> newBuider = callBuilder(classToTest);
        assertNull(newBuider.build());
    }

    @SuppressWarnings("unchecked")
    private static <POJO> PojoBuilder<POJO> callBuilder(final Class<POJO> classToTest) throws Throwable {
        final POJO instance = classToTest.newInstance();
        final Method getBuilderMethod = classToTest.getDeclaredMethod("builder");
        final Object tempBuilder = getBuilderMethod.invoke(instance);
        final PojoBuilder<POJO> builder;
        if(tempBuilder instanceof PojoBuilder<?>) {
            builder = (PojoBuilder<POJO>) tempBuilder;
        } else {
            throw new IllegalStateException(tempBuilder + " is/has not a PojoBuilder");
        }
        return builder;
    }
}
