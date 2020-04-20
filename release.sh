# 添加所有修改的内容
git add .
# 批量修改pom文件版本为相应RELEASE
sed -i "s/4.0.3-SNAPSHOT/4.0.3.RELEASE/g" pom.xml */pom.xml */*/pom.xml
# 编译打包RELEASE
mvn clean deploy
# 创建相应版本的RELEASE tag
git tag -a v4.0.3.RELEASE -m "4.0.3.RELEASE版本"
# 提交tag
git push --tags
# 添加所有修改的内容
git add .
# 提交本地分支
git commit -m '切换到4.0.3.RELEASE'
# 提交到远程分支
git push origin sccc-4.0.3
#切换到master分支
git checkout master
# 合并目标版本到master分支
git merge sccc-4.0.3
# 提交合并
git push origin master
# 创建切换到新创建的分支
git checkout -b sccc-4.0.4
# 将master提升版本号
sed -i "s/4.0.3.RELEASE/4.0.4-SNAPSHOT/g" pom.xml */pom.xml */*/pom.xml
# 添加所有修改的内容
git add .
# 提交本地master分支
git commit -m '切换到4.0.3.RELEASE'
# 提交到远程master分支
git push origin sccc-4.0.4

