![LabelZoom Logo](docs/LabelZoom_Logo_f_400px.png)

# labelzoom-moca-client-java

[![Build Status](https://github.com/labelzoom/labelzoom-moca-client-java/actions/workflows/gradle-build.yml/badge.svg?branch=main)](https://github.com/labelzoom/labelzoom-moca-client-java/actions?query=branch%3Amain)
[![Release](https://img.shields.io/github/release/labelzoom/labelzoom-moca-client-java.svg?style=flat-square)](https://github.com/labelzoom/labelzoom-moca-client-java/releases)

MOCA client for Java, sponsored by [LabelZoom](https://www.labelzoom.net).

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
