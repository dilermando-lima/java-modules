## Enjoyng JAVA MODULES jdk11+


## Some Helpfull commands and parameters
| type    | command |  short  | param                | description
| ---     |   ---   |  ---    |  ---                 |  ----
| compile | javac   |         | --module-source-path | folder from my current source with my 'module-info.java'    
| compile | javac   |   -m    | --module             | name from my module declared in 'module-info.java' and name from folder that has 'module-info.java' in it        
| compile | javac   |   -p    | --module-path        | folder with compiled/dependencies modules          
| compile | javac   |   -d    |                      | target directory to save compiling module classes         
| build   | jar     |   -e    | --main-class         | class main for runnable jar 'name-module/package.classNameMain'           
| build   | jar     |   -f    | --file               | location file target jar          
| run     | java    |   -p    | --module-path        | file with others compiled/dependencies modules  or compiled current module          
| run     | java    |   -m    | --module             | name from my module, the file module-info must be inside my module folder name
| describe| jdeps   |   -p    | --module-path        | folder with compiled/dependencies modules
| describe| jdeps   |   -m    | --module             | name from module to list dependencies        
| describe| jdeps   |   -s    | --sumary             | resume description from modules
| describe| java    |         | --list-modules       | list all modules in jre/jdk
| describe| java    |         | --describe-module    | describe dependencies in specific module
| describe| jar     |   -t    | --list               | list all files inside a jar

## Creating runnable jar non-module-jar with main class

```shell
# clean folder contains compiled source
rm -rf compiled/example.main

# compile all files '.java' from 'src/example.main' into 'compiled/example.main'
javac -d compiled/example.main $(find src/example.main -name '*.java')

# run compiled files '.class' with 'example.main.run.Print' as main class
java -cp  compiled/example.main example.main.run.Print

# create jar non-modular from compiled classes
jar --create --file=libs/example-main.1.0.jar --main-class=example.main.run.Print -C compiled/example.main .

# run created jar
java -jar libs/example-main.1.0.jar

# list all files inside created jar
jar tf  libs/example-main.1.0.jar

# checking manifest inside jar
# unzip -p  compiled/example.libs/example-lib-1.0.jar META-INF/MANIFEST.MF
```

## Creating runnable jar non-module-jar with main class

```shell

# clean folder contains compiled source
rm -rf compiled/example.main/*

# compile module named 'example.main' in 'src' folder into 'compiled' folder
javac -d compiled --module-source-path src --module example.main

# create module jar ( there is no main class )
jar --create --file=libs/example-main.2.0.jar --main-class=example.main.run.Print  -C compiled/example.main .
# jar cf libs/example-lib-1.0.jar -C compiled/example.lib .

# run created modular jar
java -jar libs/example-main.2.0.jar

# list all files inside jar
jar tf  libs/example-main.2.0.jar

# describe modules in jar
jar --file=libs/example-main.2.0.jar --describe-module
```



## Creating modular lib with no main class

```shell

# clean folder contains compiled source
rm -rf compiled/example.lib/*

# compile module named 'example.lib' in 'src' folder into 'compiled' folder
javac -d compiled --module-source-path src --module example.lib

# create module jar ( there is no main class )
jar --create --file=libs/example-lib.1.0.jar  -C compiled/example.lib .
# jar cf libs/example-lib-1.0.jar -C compiled/example.lib .

# list all files inside jar
jar tf  libs/example-lib.1.0.jar

# describe modules in jar
jar --file=libs/example-lib.1.0.jar --describe-module
```

## Creating modular app with main class using lib module
```shell

# clean folder contains compiled source
rm -rf compiled/example.app/*

# compile module named 'example.app' in 'src' folder into 'compiled' folder
javac -d compiled --module-source-path src --module example.app --module-path compiled
# #note: we added the path with lib dependency module '--module-path compiled'

# move files non-java in resource into source module ( this is no java. only moving files )
mkdir compiled/example.app/resource/ && cp -r src/example.app/resource/* compiled/example.app/resource/ 

# run compiled files on module named 'example.app' with 'example.app.run.App' as main class
java -p compiled -m example.app/example.app.run.App

# describe dependencies in modules
jdeps --module-path compiled --module example.app

# describe sumarized dependencies in modules
jdeps --module-path compiled --module example.app -s

```

## creating jre customized for our libs
```shell

# delete folder contains jre customized and clean all folder
rm -rf jre-custom
rm -rf compiled/*
rm -rf libs/* 

# recompile all modules 
javac -d compiled --module-source-path src --module example.lib
javac -d compiled --module-source-path src --module example.app --module-path compiled
mkdir compiled/example.app/resource/ && cp -r src/example.app/resource/* compiled/example.app/resource/ 

# creating our jre with 3 modules only
jlink --module-path $JAVA_HOME/jmods:compiled:libs-external   --add-modules example.lib,example.app,java.base --output jre-custom --no-header-files --no-man-pages

        # checking size from new jre
        #   du jre-custom/ -sh

        # if your new jre is not compacted ( -50MB ) use command below. You probably dont need do this in jdk14+
        #   strip -p --strip-unneeded jre-custom/lib/server/libjvm.so
        #   find jre-custom -name '*.so' | xargs -i strip -p --strip-unneeded {}

# check all modules in new jre
jre-custom/bin/java --list-modules


# run our app on new customized jre
jre-custom/bin/java --module example.app/example.app.run.App 

```

## creating jre customized for our app
```shell

# delete folder contains jre customized and clean all folder
rm -rf jre-custom &&  rm -rf compiled/* && rm -rf libs/* 

# recompile all modules 
javac -d compiled --module-source-path src --module example.lib && 
javac -d compiled --module-source-path src --module example.app --module-path compiled && 
mkdir compiled/example.app/resource/ && cp -r src/example.app/resource/* compiled/example.app/resource/ 

# creating our jre with 3 modules only and launch app
jlink --module-path $JAVA_HOME/jmods:compiled:libs-external \
--add-modules example.lib,example.app,java.base  \
--output jre-custom \
--no-header-files --no-man-pages  \
--launcher OUR-APP-IN-OUR-JRE=example.app/example.app.run.App 

# run our app
jre-custom/bin/OUR-APP-IN-OUR-JRE

```

## testing new jre with docker
```shell

# if you need clean images before or use compose-down
#   docker rmi $(docker images 'app_java_local')
#   docker rmi $(docker images -f "dangling=true" -q)
#   docker-compose --file docker/docker-compose.yml down --remove-orphans

# start container up ( may be take a few minute. It will install openjdk11, prepare jre-custom and delete openjdk11)
docker-compose --file docker/docker-compose.yml up

# go into docker
docker exec -it app_java_local /bin/sh

# run app inside container
jre-custom-docker/bin/example-readfile


# checking size jre inside docker
du jre-custom-docker # ( 33.1MB )

# checking size image docker
docker images 'app_java_local' # ( 50.6MB )


```