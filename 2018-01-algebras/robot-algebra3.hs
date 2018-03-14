module RobotAlgebra where

data Command =
  TurnLeft
  | TurnRight
  | MoveForward
  | FireLaser
  | Noop
  | AndThen Command Command

instance Monoid Command where
  mempty = Noop
  mappend c1 c2 = AndThen c1 c2

_sum :: (Monoid m, Foldable f) => f m -> m
_sum = foldl mappend mempty

type Position = (Int, Int)
type Energy = Int

data Orientation = North | East | South | West deriving (Show)

data RobotState = RobotState {
  position :: Position,
  orientation :: Orientation,
  energy :: Energy } deriving (Show)

data CommandOutcome = Success | Failure deriving (Show)

data State s a = State { runState :: (s -> (a, s)) }

insert :: a -> State s a
insert a = State $ \s -> (a, s)

get :: (s -> a) -> State s a
get f = State $ \s -> (f s, s)

update :: (s -> s) -> State s ()
update f = State $ \s -> ((), f s)

evalState :: s -> State s a -> a
evalState s st = fst $ runState st s

instance Applicative (State s) where
  pure = insert
  sf <*> sa = State $ \s -> case (runState sf s) of
    ~(f,s') -> case (runState sa s') of
      ~(a,s'') -> (f a, s'')

instance Monad (State s) where
  return = pure
  sa >>= f = State $ \ s -> case (runState sa s) of
    ~(a, s') -> runState (f a) s'

instance Functor (State s) where
  fmap f sa = sa >>= (\a -> return $ f a)

nextOrientation :: Orientation -> Orientation
nextOrientation North = East
nextOrientation East = South
nextOrientation South = West
nextOrientation West = North

prevOrientation :: Orientation -> Orientation
prevOrientation West = South
prevOrientation South = East
prevOrientation East = North
prevOrientation North = West

setEnergy :: Energy -> RobotState -> RobotState
setEnergy e' (RobotState p o e) = RobotState p o e'

setPosition :: Position -> RobotState -> RobotState
setPosition p' (RobotState p o e) = RobotState p' o e

setOrientation :: Orientation -> RobotState -> RobotState
setOrientation o' (RobotState p o e) = RobotState p o' e

changeOrientation :: (Orientation -> Orientation) -> State RobotState CommandOutcome
changeOrientation f = do
  payed <- payEnergy 1
  case (payed) of
    Success -> do
      currentOrientation <- get orientation
      update $ setOrientation $ f currentOrientation
      return Success
    Failure -> return Failure

payEnergy :: Energy -> State RobotState CommandOutcome
payEnergy cost = do
  energy <- get energy
  if (energy >= cost)
    then do
      update $ setEnergy (energy - cost)
      return Success
    else
      return Failure

executeCommand :: Command -> State RobotState CommandOutcome
executeCommand TurnLeft = changeOrientation prevOrientation
executeCommand TurnRight = changeOrientation nextOrientation
executeCommand MoveForward = do
  payed <- payEnergy 2
  case (payed) of
    Success -> do
      direction <- get orientation
      position <- get position      
      update $ setPosition $ moveInDirection position direction
      return Success
    else
      return Failure
executeCommand FireLaser = payEnergy 3
executeCommand Noop = return Success
executeCommand (AndThen c1 c2) = do
  c1r <- executeCommand c1
  case (c1r) of
    Success -> executeCommand c2
    Failure -> return Failure

moveInDirection :: Position -> Orientation -> Position
moveInDirection (x, y) North = (x, y + 1)
moveInDirection (x, y) East = (x + 1, y)
moveInDirection (x, y) South = (x, y - 1)
moveInDirection (x, y) West = (x - 1, y)
