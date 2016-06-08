due to an intellij bug https://youtrack.jetbrains.com/issue/IDEA-157271 The following steps are required to build this plugin from source

1. Download the latest Intellij EAP Build
2. Clone this Repo
3. open the project in intellij
4. in the build menu select "Prepare Plugin Module neuroph-plugin for deployment"
5. open neuroph-plugin.zip in WinRaR or 7Zip
6. go to the neuroph-plugim/libs directory and extract neuroph-plugin.jar
7. open neuroph-plugin\out\artifacts\neuroph_plugin_jar\neuroph-plugin.jar in WinRaR or 7Zip and copy all folders except the META-INF to the extracted neuroph-plugin.jar
8. copy the modified neuroph-plugin.jar back to neuroph-plugin.zip
9. install the plugin normally
10. Enjoy

Or for the easier method just download a prebuilt release
