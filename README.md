📌 Overview

This project is a Java Swing GUI application that combines:
	•	Huffman Encoding (for text compression/representation)
	•	MD5 Hashing (to generate unique keys for messages)
	•	File-based Storage (to save and retrieve encrypted messages using MD5 keys)

The app allows you to:
	1.	Encrypt a message → Generates Huffman encoding + MD5 hash, then saves the message in a file (md5_storage.txt).
	2.	Decrypt a message → Retrieve the original message using its MD5 hash.

⸻

🚀 Features
	•	Huffman Encoding: Builds a Huffman tree to encode the input text into binary codes.
	•	MD5 Hashing: Creates a fixed-size 128-bit hash for the input text.
	•	Persistent Storage: Saves mappings of MD5 → Original message in a file (md5_storage.txt).
	•	Decryption: Enter an MD5 key to retrieve the original message.
	•	Java Swing GUI: Easy-to-use graphical interface.
