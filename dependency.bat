call mvn dependency:tree

# 查看包结构树
# call mvn dependency:tree -Dverbose -Dincludes=org.apache.curator:curator-recipes

# 全部包导出到lib
# call mvn dependency:copy-dependencies -DoutputDirectory=lib

# 只导出关联包到lib
# call mvn dependency:copy-dependencies -DoutputDirectory=lib -DincludeScope=compile

# 出WAR包
# call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -Dversion=1.0.0-SNAPSHOT -DarchetypeArtifactId=maven-archetype-webapp -DarchetypeCatalog=internal

# 出POM
# call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-site-simple -DarchetypeCatalog=internal

# 出JAR包
# call mvn archetype:generate -DgroupId=nepxion -DartifactId=coroutine-common -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false -DarchetypeCatalog=internal 

# 加入到本地库
# call mvn install:install-file -DgroupId=kilim -DartifactId=kilim -Dversion=1.0 -Dfile=lib/kilim-1.0.jar -Dpackaging=jar -DgeneratePom=true

pause