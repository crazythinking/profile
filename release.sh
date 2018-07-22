# 根据当前分支新建本地分支，并切换到新分支
git checkout -b dev-2.1.0
# 推送当前本地分支并建立本地到上游（远端）仓的链接
git push --set-upstream origin dev-2.1.0
# 批量修改pom文件版本为相应RELEASE
sed -i "s/2.1.0-SNAPSHOT/2.1.0.RELEASE/g" pom.xml */pom.xml */*/pom.xml
# 编译打包RELEASE
# mvn clean deploy
# 创建相应版本的RELEASE tag

# 重新checkout到master
git checkout master
# 将master提升版本号
sed -i "s/2.1.0.RELEASE/2.1.1-SNAPSHOT/g" pom.xml */pom.xml */*/pom.xml
# 添加所有修改的内容
git add .
# 提交本地master分支
git commit -m '切换到2.1.1-SNAPSHOT'
# 提交到远程master分支
git push origin master

