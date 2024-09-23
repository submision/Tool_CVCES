const { defineConfig } = require('@vue/cli-service')
const path = require('path')
chainWebpack: config => {
  // 修复HMR
  config.resolve.symlinks(true);
},
module.exports = defineConfig({
  transpileDependencies: true
})
module.exports = { configureWebpack: {
  resolve: {
    extensions: [".ts", ".tsx", ".js", ".json", ".vue"],  
     alias: {}
  },
  devServer: {
    port: '8099',
    // 热更新
    host: 'localhost',
    //hot: true,
  },
  module: {        
    rules: [    
      {    
        test: /\.tsx?$/,    
        loader: 'ts-loader',    
        exclude: /node_modules/,    
        options: {
          appendTsSuffixTo: [/\.vue$/],    
        }    
      }        
    ]    
  },   
},
}
 
 
