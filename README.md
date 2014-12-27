Creates a word search grid with one instance of the string you choose, and fills in the rest of the grid with characters from that string or random characters. Uses a special datastructure and algorithm to keep track of additional possible instances and prevents character insertions that would make the chosen word.

````
WordSearchGenerator w = new WordSearchGenerator(3, 3, "run", FillType.CharactersOfTheWord);
w.build();
````

####2 step process
1) Inserts one instance of the input string in a random orientation and location

````
0r0
0u0
0n0
````
2) Fills the rest of the grid with letters from the input string

````
rrn
uur
unu
````

#####Example input / output - Can you find the word?

````
WordSearchGenerator w = new WordSearchGenerator(7, 7, "hello", FillType.CharactersOfTheWord);
w.build();

eelello
lleoool
hhelhel
elllole
leoehlo
hhlelel
loehhlh

````

````
WordSearchGenerator w = new WordSearchGenerator(8, 8, "coding", FillType.RandomCharacters);
w.build();

qbivqaoa
lxcajhsx
ixbfkwsq
mnzdawhf
qtvcwzwu
fdovwkwb
gnidocmn
qqtwsqby
````
