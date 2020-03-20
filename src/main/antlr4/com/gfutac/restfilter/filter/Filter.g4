grammar Filter;

parse
 : expression EOF
 ;


expression
 : left=expression op=AND right=expression         #binaryAndExpression
 | left=expression op=OR right=expression        #binaryOrExpression
 | left=IDENTIFIER op=comparator right=value      #comparatorExpression
 | LPAREN expression RPAREN                       #parenExpression
 ;

comparator
 : GT | GE | LT | LE | EQ | NE | LIKE
 ;

value
 : DECIMAL | STRING;

AND        : 'AND' ;
OR         : 'OR' ;

GT         : '>' ;
GE         : '>=' ;
LT         : '<' ;
LE         : '<=' ;
EQ         : '=' ;
NE         : '!=';
LIKE       : '~' ;

LPAREN     : '(' ;
RPAREN     : ')' ;

DECIMAL    : '-'? [0-9]+ ( '.' [0-9]+ )? ;
STRING     : '"' ('\\' (["\\/bfnrt] | 'u' [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F] [0-9a-fA-F]) | ~ ["\\\u0000-\u001F])* '"';

IDENTIFIER : [a-zA-Z_$]('.'?[a-zA-Z_$0-9])*;
WS         : [ \r\t\u000C\n]+ -> skip;