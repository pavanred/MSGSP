###CS583 - Data Mining and Text Mining

###Implementation of MS-GSP algorithm
Mining Sequential Patterns Based on GSP (Generalized Sequential Patterns)
MS-GSP algorithm - Sequential pattern mining using multiple minimum supports with a support difference constraint.

###Input (Format)
data.txt

<{25, 37, 47}{48}> -- This is a transaction / sequence

{25, 37, 47}       -- represents an itemset and each number represents an individual item

para.txt

MIS(1) = 0.003     -- Minimum item support for item 1

SDC = 0.05         -- Support difference constraint value.

###Output (Format)

Pattern : <{2}{5,13}{14}{7}> Count : 1 -- The sequential pattern and its count in the transactions
