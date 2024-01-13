![LabelZoom Logo](docs/LabelZoom_Logo_f_400px.png)

# labelzoom-moca-client-java

## How To Use
See [tests](src/test) for examples.
```java
try (final MocaConnection conn = new HttpMocaConnection(url, userId, password))
{
    final ResultSet res = conn.execute("publish data where message = 'Hello World!'");
    res.next();
    System.out.println("Message: " + res.getString("message"));
}
catch (final SQLException | IOException e)
{
    e.printStackTrace();
}
```
