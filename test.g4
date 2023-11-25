grammar KotlinDependency;

compilationUnit: packageDeclaration? importDeclaration* classDeclaration*;

packageDeclaration: 'package' qualifiedName SEMI;

importDeclaration: 'import' qualifiedName SEMI;

classDeclaration: 'class' className (':' type)? classBody SEMI;

classBody: '{' classMember* '}';

classMember: functionDeclaration | propertyDeclaration;

functionDeclaration: 'fun' IDENTIFIER '(' parameterList? ')' (':' type)? block;

propertyDeclaration: 'val' propertyList (':' type)? SEMI;

propertyList: IDENTIFIER ('=' expression)?;

parameterList: parameter (',' parameter)*;

parameter: IDENTIFIER ':' type;

type: qualifiedName;

qualifiedName: IDENTIFIER ('.' IDENTIFIER)*;

expression: IDENTIFIER | INT | STRING | '(' expression ')' | expression '.' IDENTIFIER;

block: '{' statement* '}';

statement: expression SEMI;

INT: [0-9]+;
STRING: '"' .*? '"';
IDENTIFIER: [a-zA-Z_] [a-zA-Z_0-9]*;
SEMI: ';';
WS: [ \t\r\n]+ -> 