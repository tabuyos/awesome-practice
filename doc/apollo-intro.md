```shell
git clone git@github.com:apolloconfig/apollo.git
cd apollo
./script/build
```

```shell
# Windoew:
@echo off
start cmd /c "java -jar apollo-adminservice-2.0.0-SNAPSHOT.jar"
start cmd /c "java -jar apollo-configservice-2.0.0-SNAPSHOT.jar"
start cmd /c "java -jar apollo-portal-2.0.0-SNAPSHOT.jar"
pause

# Linux:
sh -c "java -jar apollo-adminservice-2.0.0-SNAPSHOT.jar"
sh -c "java -jar apollo-configservice-2.0.0-SNAPSHOT.jar"
sh -c "java -jar apollo-portal-2.0.0-SNAPSHOT.jar"
```

