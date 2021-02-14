# Uji Beban Application

## Pre-requisite

* Java 8+

## Development

### Initialization

```shell
./cmd-init
```

### Format source code

```shell
./cmd-format-code
```

### Build application

```shell
./cmd-build
```

### Adding request configuration

Add <code>config</code> directory at the same level of application jar file. The convention should be like
<code>config/*/application.yml</code>, example:

```shell
- ujibean-app.jar
- config
  |- your-dir-1
     |- application.yml
  |- your-dir-2
     |- application.yml
```

### Run application

```shell
./cmd-run
```