module ParserAlgebra where

import Control.Monad.Trans.Class
import Data.Traversable

data Identity a = Identity a

instance Functor Identity where
  fmap f fa = fa >>= (\a -> return $ f a)

instance Applicative Identity where
  pure = Identity
  (<*>) (Identity f) (Identity a) = Identity $ f a

instance Monad Identity where
  (>>=) (Identity a) f = f a

data StateT s m a = StateT { runStateT :: (s -> m (a, s)) }

insert :: (Monad m) => a -> StateT s m a
insert a = StateT $ \ s -> return (a, s)

get :: (Monad m) => (s -> a) -> StateT s m a
get f = StateT $ \ s -> return (f s, s)

put :: (Monad m) => (s -> s) -> StateT s m ()
put f = StateT $ \ s -> return ((), f s)

evalStateT :: (Functor m) => s -> StateT s m a -> m a
evalStateT s st = fmap fst $ runStateT st s

type State s = StateT s Identity

-- insert :: a -> State s a
-- insert a = State (\s -> (a, s))
--
-- get :: (s -> a) -> State s a
-- get f = State (\s -> (f s, s))
--
-- put :: (s -> s) -> State s ()
-- put f = State (\s -> ((), f s))
--
-- eval :: (State s a) -> s -> a
-- eval (State run) s = fst $ run s

instance (Functor m) => Functor (StateT s m) where
  fmap f m = StateT $ \ s ->
      fmap (\ ~(a, s') -> (f a, s')) $ runStateT m s

instance (Functor m, Monad m) => Applicative (StateT s m) where
  pure a = StateT $ \ s -> return (a, s)
  StateT mf <*> StateT mx = StateT $ \ s -> do
      ~(f, s') <- mf s
      ~(x, s'') <- mx s'
      return (f x, s'')

instance (Monad m) => Monad (StateT s m) where
  m >>= k = StateT $ \ s -> do
    ~(a, s') <- runStateT m s
    runStateT (k a) s'

instance MonadTrans (StateT s) where
  lift m = StateT $ \ s -> do
    a <- m
    return (a, s)

data ParserResult a =
  ParserError String
  | ParserSuccess a
  deriving (Show)

instance Functor ParserResult where
  fmap f pra = pra >>= (\a -> pure $ f a)

instance Applicative ParserResult where
  pure = ParserSuccess
  (<*>) prf pra = case (prf) of
    ParserError e1 -> ParserError e1
    ParserSuccess f -> case (pra) of
      ParserError e2 -> ParserError e2
      ParserSuccess a -> pure $ f a

instance Monad ParserResult where
  (>>=) pr f = case (pr) of
    ParserSuccess a -> f a
    ParserError e -> ParserError e

data ParserState i = ParserState { idx :: Int, elems :: [i] } deriving (Show)

type Parser i = StateT (ParserState i) ParserResult

parse :: [i] -> Parser i o -> ParserResult o
parse input parser = evalStateT (ParserState 0 input) parser

expectedError :: (Show a) => a -> Int -> a -> String
expectedError e idx a = "Expected " ++ (show e) ++ " at index " ++ (show idx)
  ++ " but got " ++ (show a) ++ "instead"

elem :: (Eq a, Show a) => a -> Parser a a
elem a = do
  index <- get idx
  xs <- get elems
  let x = xs !! index
  if (x == a)
    then do; put (\s -> ParserState (succ index) xs); return a
    else lift $ ParserError $ expectedError a index x

-- add :: Parser i [o] -> Parser i o -> Parser i [o]
-- add (State r1) (State r2) = State (\ps ->
--   case (r1 ps) of
--     (ParserError e1, s1) -> (ParserError e1, s1)
--     (ParserSuccess o1, s1) -> case (r2 s1) of
--       (ParserError e2, s2) -> (ParserError e2, s2)
--       (ParserSuccess o2, s2) -> (ParserSuccess $ o1 ++ [o2], s2))
