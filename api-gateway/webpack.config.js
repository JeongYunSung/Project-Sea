const path = require('path')
const webpack = require('webpack')
const { CleanWebpackPlugin } = require( 'clean-webpack-plugin')
const HtmlWebpackPlugin = require("html-webpack-plugin")
const NodeExternals = require('webpack-node-externals');

module.exports = {
    entry: {
        main: path.resolve("./src/app.ts")
    },
    mode: "development",
    output: {
        path: path.resolve("./dist"),
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                exclude: /(node_modules)|(dist)/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.ts$/,
                exclude: /(node_modules)|(dist)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env', '@babel/preset-typescript'],
                        plugins: ['@babel/plugin-proposal-class-properties', "@babel/plugin-proposal-object-rest-spread"]
                    }
                }
            }
        ]
    },
    plugins: [
        new webpack.BannerPlugin({
            banner: `
                Build Time : ${new Date().toLocaleTimeString()}
            `
        }),
        new CleanWebpackPlugin(),
        new HtmlWebpackPlugin()
    ],
    externals: NodeExternals(),
    target: 'node',
    resolve: {
        extensions: ['.ts', '.json']
    }
}