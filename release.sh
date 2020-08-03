# 添加所有修改的内容
git add .
# 批量修改pom文件版本为相应RELEASE
sed -i "s/2.6.1-SNAPSHOT/2.6.1.RELEASE/g" pom.xml */pom.xml */*/pom.xml
# 添加所有修改的内容
git add .
# 编译打包RELEASE
mvn clean deploy
# 添加所有修改的内容
git add .
# 提交本地分支
git commit -m '切换到2.6.1.RELEASE'
# 提交到远程分支
git push origin 2.6.1
# 创建相应版本的RELEASE tag
git tag -a v2.6.1.RELEASE -m "2.6.1.RELEASE版本"
# 提交tag
git push --tags
#切换到master分支
git checkout master
# 合并目标版本到master分支
git merge 2.6.1
# 添加所有修改的内容
git add .
# 提交合并
git push origin master
# 创建切换到新创建的分支
git checkout -b 2.6.2
# 将master提升版本号
sed -i "s/2.6.1.RELEASE/2.6.2-SNAPSHOT/g" pom.xml */pom.xml */*/pom.xml
# 添加所有修改的内容
git add .
# 提交本地master分支
git commit -m '版本号升级2.6.2-SNAPSHOT'
# 提交到远程master分支
git push origin 2.6.2

