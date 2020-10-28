const path = require('path')
const webpack = require('webpack')
const { CleanWebpackPlugin } = require( 'clean-webpack-plugin')
const HtmlWebpackPlugin = require("html-webpack-plugin")

module.exports = {
    entry: {
        main: './src/index.ts'
    },
    mode: 'development',
    output: {
        path: path.resolve('./dist'),
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.ts$/,
                exclude: /(node_module)|(dist)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-typescript'],
                        plugins: ['@babel/plugin-proposal-object-rest-spread', '@babel/plugin-proposal-class-properties']
                    }
                }
            }
        ]
    },
    plugins: [
        new webpack.BannerPlugin({
            banner: `
                BuildTime : ${new Date().toLocaleTimeString()}
            `
        }),
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin()
    ]
}