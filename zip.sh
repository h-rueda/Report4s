file=report4s-5.0
rm ${file}.zip
cp jar/${file}.jar lib/
zip ${file}.zip CHANGELOG LICENSE README* report4s.properties lib/commons-io-*.jar lib/commons-lang3-*.jar lib/report4s-*.jar -r javadoc
rm lib/${file}.jar
