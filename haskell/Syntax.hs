module Syntax where

data FuncDef = FuncDef String Instruction

pShowFuncDef :: FuncDef -> String
pShowFuncDef (FuncDef name inst) =
    "def " ++ name ++ " as\n" ++ (pShowInstruction 1 inst)

data Instruction
    = Compose Instruction Instruction
    | If Cond SenseDir Instruction Instruction
    | Mark MkType
    | Unmark MkType
    | PickUp
    | PickUpElse Instruction
    | Drop
    | Turn LR
    | Move
    | MoveElse Instruction
    | Flip Int Instruction Instruction
    | Pass
    deriving (Eq, Ord)

pShowInstruction :: Int -> Instruction -> String
pShowInstruction i inst = case inst of
    Compose i1 i2 -> (pShowInstruction i i1) ++ "\n" ++ (pShowInstruction i i2)

data SenseDir
    = Here
    | Ahead
    | LeftAhead
    | RightAhead
    deriving (Eq, Ord)

data Cond
    = Friend
    | FriendFood
    | Foe
    | FoeFood
    | Food
    | Rock
    | Marker MkType
    deriving (Eq, Ord)

data LR = Left | Right deriving (Eq, Ord)
data MkType = MkA | MkB | MkC | MkD | MkE | MkF deriving (Eq, Ord)
