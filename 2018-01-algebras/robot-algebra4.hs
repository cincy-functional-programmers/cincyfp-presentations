module RobotAlgebra where

import Control.Monad.Trans.Class

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

data StateT s m a = StateT { runStateT :: (s -> m (a, s)) }

insert :: (Monad m) => a -> StateT s m a
insert a = StateT $ \s -> return (a, s)

get :: (Monad m) => (s -> a) -> StateT s m a
get f = StateT $ \s -> return (f s, s)

update :: (Monad m) => (s -> s) -> StateT s m ()
update f = StateT $ \s -> return ((), f s)

evalStateT :: (Functor m) => s -> StateT s m a -> m a
evalStateT s st = fmap fst $ runStateT st s

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
  currentOrientation <- get orientation
  update $ setOrientation $ f currentOrientation

payEnergy :: Energy -> StateT RobotState CommandOutcome ()
payEnergy cost = do
  energy <- get energy
  if (energy >= cost)
    then update $ setEnergy (energy - cost)
    else lift $ Failure InsufficientEnergy

executeCommand :: Command -> StateT RobotState CommandOutcome ()
executeCommand TurnLeft = changeOrientation prevOrientation
executeCommand TurnRight = changeOrientation nextOrientation
executeCommand MoveForward = do
  payEnergy 2
  direction <- get orientation
  position <- get position  
  update $ setPosition $ moveInDirection position direction
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
