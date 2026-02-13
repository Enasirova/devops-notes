with infocmp we could generate easier with tput

tput clear: clears terminal

tput: generates escape sequence.

it moved my coursor:

![](images/screenshot-20260211-104744.png)

then when i type anything it goes back:

![](images/screenshot-20260211-104809.png)

its just escape sequesnces generation and it terminal turn them into actions:

![](images/screenshot-20260211-105017.png)

`setaf` : front color
`setab`: background color

![](images/screenshot-20260211-105235.png)

here we will see how many colors, columns (how many characters, depending on the zooming) and lines our terminal supports:

![](images/screenshot-20260211-105341.png)

![](images/screenshot-20260211-105513.png)

![](images/screenshot-20260211-105522.png)

# extra

bash needs to know how long our PS1 prompt is (calculated 
automatically)

escape characters are the problem to calculate: then the line can be broken - lesss characters can be allowed. line wraps in the wrong places.

our shell can only communicate with our terminal certain way. it needs to konw how long our prompt is, cause by default calculation of characters is off. it leads to inconsistencies.

how we can solve it?

we need to wrap all escape sequences into `\[... \]` -> then escape sequences won't be counted as normal characters

![](images/screenshot-20260211-113550.png)



current folder and the arrow. use doublequots
if we use \ -> its escaping, so \$ will be regular dollar sign

you can use all unicode charactesrs what terminal supports (emogies for ex.). google for unicode character error/emoji etc. 

PS1="....$(tput sgr0)"