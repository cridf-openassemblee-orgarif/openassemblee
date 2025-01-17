
# HEMICYCLE
trash src/main/webapp/hemicycle
cd hemicycle
yarn
NODE_OPTIONS=--openssl-legacy-provider yarn build
cd ..
cp -R hemicycle/build/static/js src/main/webapp/hemicycle

# FRONT
npm install
grunt build

# GIT REVISION
echo "$(git log -1 --pretty=%h)" > "src/main/webapp/dist/version.txt"

# BACK
trash target
mvn install -Pprod

# PACKAGE
trash .package
mkdir .package
mkdir .package/region
mkdir .package/clevercloud
cp target/openassemblee-1.0-SNAPSHOT.war.original .package/region/openassemblee.war
cp target/openassemblee-1.0-SNAPSHOT.war .package/clevercloud/openassemblee.war
