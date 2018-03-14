module RobotAlgebra where

import Control.Applicative
import Control.Monad.Trans.Class
import Control.Monad.Trans.State.Lazy

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

data RobotFailure =
  InsufficientEnergy
  | InvalidMove
  | UnknownError
  deriving (Show)

data CommandOutcome a =
  Success a
  | Failure RobotFailure
  deriving (Show)

instance Applicative CommandOutcome where
  pure = Success
  cof <*> ca = do
    f <- cof
    a <- ca
    return $ f a

instance Monad CommandOutcome where
  return = pure
  (Success a) >>= f = f a
  (Failure rf) >>= _ = Failure rf

instance Functor CommandOutcome where
  fmap f coa = coa >>= (\a -> return $ f a)

instance Alternative CommandOutcome where
  empty = Failure UnknownError
  ca <|> cb = case (ca) of
    (Failure _) -> cb
    otherwise -> ca

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

changeOrientation :: (Orientation -> Orientation) -> StateT RobotState CommandOutcome ()
changeOrientation f = do
  payEnergy 1
  currentOrientation <- gets orientation
  modify $ setOrientation $ f currentOrientation

payEnergy :: Energy -> StateT RobotState CommandOutcome ()
payEnergy cost = do
  energy <- gets energy
  if (energy >= cost)
    then modify $ setEnergy (energy - cost)
    else lift $ Failure InsufficientEnergy

executeCommand :: Command -> StateT RobotState CommandOutcome ()
executeCommand TurnLeft = changeOrientation prevOrientation
executeCommand TurnRight = changeOrientation nextOrientation
executeCommand MoveForward = do
  payEnergy 2
  direction <- gets orientation
  position <- gets position
  modify $ setPosition $ moveInDirection position direction
executeCommand FireLaser = payEnergy 3
executeCommand Noop = return ()
executeCommand (AndThen c1 c2) = do
  executeCommand c1
  executeCommand c2

moveInDirection :: Position -> Orientation -> Position
moveInDirection (x, y) North = (x, y + 1)
moveInDirection (x, y) East = (x + 1, y)
moveInDirection (x, y) South = (x, y - 1)
moveInDirection (x, y) West = (x - 1, y)
