trash src/main/webapp/hemicycle
cd hemicycle
yarn
NODE_OPTIONS=--openssl-legacy-provider yarn build
cd ..
cp -R hemicycle/build/static/js src/main/webapp/hemicycle
