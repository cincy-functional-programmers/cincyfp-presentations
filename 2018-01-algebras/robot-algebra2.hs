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

data RobotState = RobotState Position Orientation Energy deriving (Show)

data CommandOutcome = Success | Failure deriving (Show)

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

executeCommand :: RobotState -> Command -> (CommandOutcome, RobotState)
executeCommand rs@(RobotState pos dir nrg) TurnLeft = if (nrg > 0)
  then (Success, RobotState pos (prevOrientation dir) (nrg - 1))
  else (Failure, rs)
executeCommand rs@(RobotState pos dir nrg) TurnRight = if (nrg > 0)
  then (Success, RobotState pos (nextOrientation dir) (nrg - 1))
  else (Failure, rs)
executeCommand rs@(RobotState pos dir nrg) MoveForward = if (nrg > 1)
  then (Success, RobotState (moveInDirection pos dir) dir (nrg - 2))
  else (Failure, rs)
executeCommand rs@(RobotState pos dir nrg) FireLaser = if (nrg > 2)
  then (Success, RobotState pos dir (nrg - 3))
  else (Failure, rs)
executeCommand rs Noop = (Success, rs)
executeCommand rs (AndThen c1 c2) =
  case (executeCommand rs c1) of
    (Failure, rs') -> (Failure, rs')
    (Success, rs') -> executeCommand rs' c2

moveInDirection :: Position -> Orientation -> Position
moveInDirection (x, y) North = (x, y + 1)
moveInDirection (x, y) East = (x + 1, y)
moveInDirection (x, y) South = (x, y - 1)
moveInDirection (x, y) West = (x - 1, y)
