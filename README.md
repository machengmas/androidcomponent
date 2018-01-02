AndroidComponent
======
Download
--------

Download [the latest JAR][3] or grab via Maven: 
Step 1. Add the JitPack repository to your build file
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Step 2. Add the dependency
```xml
<dependency>
    <groupId>com.github.machengmas</groupId>
    <artifactId>androidcomponent</artifactId>
    <version>1.0-beta1</version>
</dependency>
```
or Gradle:
Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
	allprojects {
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency
```groovy
dependencies {
    compile 'com.github.machengmas:androidcomponent:1.0-beta1'
}
```

