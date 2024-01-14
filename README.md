![LabelZoom Logo](docs/LabelZoom_Logo_f_400px.png)

# labelzoom-moca-client-java
MOCA client for Java, sponsored by [LabelZoom](https://www.labelzoom.net).

## Installation

### Gradle
#### Step 1
Add a new `maven` closure to your `repositories` section that points to this repository.
```groovy
repositories {
    mavenCentral() // You probably already have this
    maven {
        url = uri('https://maven.pkg.github.com/labelzoom/labelzoom-moca-client-java')
    }
}
```

#### Step 2
Add the dependency to your `implementation` dependencies:
```groovy
dependencies {
    implementation 'com.labelzoom:labelzoom-moca-client-java:1.0.1'
}
```

### Maven
TODO

## How To Use
See [tests](src/test) for more examples.

### Nested _try-with-resources_
```java
try (final MocaConnection conn = new HttpMocaConnection(url, userId, password))
{
    try (final ResultSet res = conn.execute("publish data where message = 'Hello World!'"))
    {
        res.next();
        System.out.println("Message: " + res.getString("message"));
    }
}
catch (final SQLException | IOException e)
{
    e.printStackTrace();
}
```

### Single _try-with-resources_ with multiple resources
```java
try (final MocaConnection conn = new HttpMocaConnection(url, userId, password);
     final ResultSet res = conn.execute("publish data where message = 'Hello World!'"))
{
    res.next();
    System.out.println("Message: " + res.getString("message"));
}
catch (final SQLException | IOException e)
{
    e.printStackTrace();
}
```
