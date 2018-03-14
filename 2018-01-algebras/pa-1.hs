module Parsing where

data ParserResult o =
  ParserSuccess o
  | ParserError String
  deriving (Show)

type Parser i o = (i -> ParserResult o)

elemParser :: (Eq a, Show a) => a -> Parser a a
elemParser a = \ i -> if (i == a)
  then ParserSuccess i
  else ParserError $ "Expected " ++ (show a) ++ " but got " ++ (show i)
