file=report4s-5.2
rm ${file}.zip
cp jar/${file}.jar lib/
zip ${file}.zip CHANGELOG LICENSE README* report4s.properties lib/commons-io-*.jar lib/commons-lang3-*.jar lib/report4s-*.jar -r javadoc
rm lib/${file}.jar
