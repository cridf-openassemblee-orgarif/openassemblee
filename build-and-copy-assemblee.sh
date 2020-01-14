trash src/main/webapp/assemblee
cd assemblee
yarn
yarn build
cd ..
cp -R assemblee/build/static/js src/main/webapp/assemblee