grammar Filter;

parse
 : expression EOF
 ;


expression
 : LPAREN expression RPAREN                         #parenExpression
 | left=expression op=AND right=expression          #binaryAndExpression
 | left=expression op=OR right=expression           #binaryOrExpression
 | left=IDENTIFIER op=comparator right=value        #comparatorExpression
 ;

comparator
 : GT | GE | LT | LE | EQ | NE | LIKE | NLIKE
 ;

value
 : NUMERIC | STRING | DATE | BOOL | NULL;

AND        : 'AND'|'and' ;
OR         : 'OR'|'or' ;

GT         : '>' ;
GE         : '>=' ;
LT         : '<' ;
LE         : '<=' ;
EQ         : '=' ;
NE         : '!=';
LIKE       : '~' ;
NLIKE      : '!~' ;

LPAREN     : '(' ;
RPAREN     : ')' ;

NUMERIC    : '-'? [0-9]+ ( '.' [0-9]+ )? ;
STRING     : '"' ('\\' (["\\/bfnrt] | 'u' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]) | ~ ["\\\u0000-\u001F])* '"';
DATE       : 'date"' YEAR '-' MONTH '-' DAY 'T' HOUR ':' MIN ':' SEC '.' MILIS 'Z' '"';
NULL       : 'NULL';
BOOL       : 'TRUE'|'true'|'FALSE'|'false';

IDENTIFIER : [a-zA-Z_$]('.'?[a-zA-Z_$0-9])*;
WS         : [ \r\t\u000C\n]+ -> skip;


fragment DIGIT : '0'..'9';
fragment YEAR : DIGIT DIGIT DIGIT DIGIT;
fragment MONTH : DIGIT DIGIT;
fragment DAY : DIGIT DIGIT;
fragment HOUR : DIGIT DIGIT;
fragment MIN : DIGIT DIGIT;
fragment SEC : DIGIT DIGIT;
fragment MILIS : DIGIT DIGIT DIGIT;