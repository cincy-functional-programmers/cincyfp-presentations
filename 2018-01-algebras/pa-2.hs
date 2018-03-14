module Parsing where

data ParserResult o =
  ParserSuccess o
  | ParserError String
  deriving (Show)

data ParserState i = ParserState { index :: Int, input :: [i] }

type Parser i o = (ParserState i -> (ParserResult o, ParserState i))

elemParser :: (Eq a, Show a) => a -> Parser a a
elemParser a = \ i -> if (i == a)
  then ParserSuccess i
  else ParserError $ "Expected " ++ (show a) ++ " but got " ++ (show i)
