grammar BoolExpression;

@header {
package fq.bool;
}

parse
 : expression EOF
 ;

expression
 : LPAREN expression RPAREN                       #parentExpr
 | NOT expression                                 #negateExpr
 | left=expression op=binary right=expression     #binaryExpr
 | left=primary cp=comparator right=primary       #compareExpr
 ;

primary
 : bool                                           #boolExpr
 | IDENTIFIER                                     #idExpr
 | DECIMAL                                        #numExpr
 ;

comparator
 : GT | GE | LT | LE | EQ
 ;

binary
 : AND | OR
 ;

bool
 : TRUE | FALSE
 ;

AND        : 'AND' ;
OR         : 'OR' ;
NOT        : 'NOT';
TRUE       : 'TRUE' ;
FALSE      : 'FALSE' ;
GT         : '>' ;
GE         : '>=' ;
LT         : '<' ;
LE         : '<=' ;
EQ         : '=' ;
LPAREN     : '(' ;
RPAREN     : ')' ;
DECIMAL    : '-'? [0-9]+ ( '.' [0-9]+ )? ;
IDENTIFIER : [a-zA-Z_] [a-zA-Z_0-9]* ;
WS         : [ \r\t\u000C\n]+ -> skip;