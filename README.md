***

<h1 align="center">
    <img  src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/icon.png" height="46" width="46">
    Checksum Calculator 0.1 beta
</h1>
<p align="center">
    A simple, lightweight JavaFX application for generating checksum hashes		
    <br><br>
    <sub> Currently supports:  MD5,  SHA-1,  SHA-256,  SHA-384, and  SHA-512 </sub>
</p>

***

## Download

[Checksum_Calculator.jar](https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/Checksum_Calculator.jar) (480 KB)

To run, just double-click the file, or run `java -jar Checksum Calculator.jar` in the command line. 

This application needs [Java 8](https://www.java.com/en/download/) or higher installed

## How to use

On startup, the application looks like this: <br>
<p align="center">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/startup.png">
</p>

<br>

The first dropdown contains all the possible algorithms, with MD5 selected by default. Currently, the supported algorithms are MD5, SHA-1, SHA-256, SHA-384, and SHA-512. More will be added in future releases or when new standards are announced. <br>
<p align="center">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/algorithms.png">
</p>

<br>

The second dropdown shows the possible input selection types. With the `Text` option, you can simply type text in the top text-bar to generate a hash. When `File` is selected (default), the `Open` button is enabled, and you can select any file from the filesystem instead. <br>
<p align="center">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/text.png">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/fileloaded.png">
</p>

<br>

The `Generate` button starts hashing the text or file selected. If you want to halt the hash generation safely, (files over 200MB will take a few seconds or longer), you can click the `Stop` button. <br>
<p align="center">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/textgenerated.png">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/generating.png">
</p>
The `Copy` button copies the hash from the `Result:` section to your clipboard.

<br>

When the `Compare To:` box is checked, any text pasted (with the `Paste` button) in the `Compare To:` section will be automatically compared to the hash from the `Result:` section. If both match, they will be highlighted green, otherwise they will be highlighted red.
<br><br>
Note: since the longest hash result (SHA-512) is 128 characters long, the longest string that can be pasted in must be 128 characters. <br>
<p align="center">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/match.png">
    <img src="https://raw.githubusercontent.com/droppinstackz/Checksum_Calculator/master/docs/non-match.png">
</p>

<br>

The application will also remember your previous selections for algorithm type, input selection, and if the `Compare To:` check-box is checked. (The settings are saved under the user's home directory in a folder called `.checksumCalc`)

## About

This application is licensed with GNU General Public License Version 2

All the screenshots were taken on a KDE Breeze (Linux) desktop.