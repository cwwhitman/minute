implementation details

use rust for the backend

instructions for chris:
 - make parser for language
   - make rust project to serve as an entry point (with cli args, etc)
   - think about what types could be used to represent the AST
   - use rust parser combinator library to make parser
 - make scheduler
   - make this separate from the parser, connected through entry point file
   - implement each command to do what it wants, maybe come up with data structure to
   represent time
