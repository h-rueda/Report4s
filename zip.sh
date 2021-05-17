file=report4s-4.0
rm ${file}.zip
cd Report4s
cp jar/${file}.jar lib/
zip ../${file}.zip CHANGELOG LICENSE README report4s.properties lib/commons-io-*.jar lib/guava-*.jar lib/poi-*.jar lib/xmlbeans-*.jar lib/${file}.jar -r javadoc -0
rm lib/${file}.jar

