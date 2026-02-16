we autocomplete a filename that contains a whitespace character. bash might autocomplete it to:

`cat a\ file.txt`

\ will disable normal behaviour

![](images/screenshot-20260213-165032.png)

excaping would also allow us to print a single double quote in the following way:

![](images/screenshot-20260213-165110.png)

you coudl also do `'"'`
![](images/screenshot-20260213-165201.png)

doesnt work with single quotes..:
![](images/screenshot-20260213-165243.png)

signle quotes disable ALL rewrites/expansions -> so even backlash is disabled, here it means that last single quote and then there is no termination of command ..:

![](images/screenshot-20260213-165337.png)
