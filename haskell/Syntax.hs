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
    If cnd sdir i1 i2 ->
        tabs i ++ "if " ++ show cnd ++ ' ' : show sdir ++ " do\n" 
            ++ pShowInstruction (i + 1) i1 ++ "\n"
            ++ tabs i ++ "else\n"
            ++ pShowInstruction (i + 1) i2 ++ "\n"
            ++ tabs i ++ "end\n"
    Pass -> tabs i ++ "pass"

-- Generate a string of spaces that corresponds to the given number of tabs of
-- width 4.
tabs :: Int -> String
tabs = tabsAcc ""
  where
    tabsAcc acc 0 = acc
    tabsAcc acc i = tabsAcc (acc ++ "    ") (i - 1)

data SenseDir
    = Here
    | Ahead
    | LeftAhead
    | RightAhead
    deriving (Eq, Ord)

instance Show SenseDir where
    show sd = case sd of
        Here       -> "here"
        Ahead      -> "ahead"
        LeftAhead  -> "leftahead"
        RightAhead -> "rightahead"

data Cond
    = Friend
    | FriendFood
    | Foe
    | FoeFood
    | Food
    | Home
    | FoeHome
    | Rock
    | Marker MkType
    deriving (Eq, Ord)

instance Show Cond where
    show _ = "SHOW COND NOT YET IMPLEMENTED"

data LR = ALeft | ARight deriving (Eq, Ord)
instance Show LR where
    show lr = case lr of { ALeft -> "left" ; ARight -> "right" }

data MkType = MkA | MkB | MkC | MkD | MkE | MkF deriving (Eq, Ord)
instance Show MkType where
    show mk = case mk of { MkA -> "A" ; MkB -> "B" ; MkC -> "C" ;
        MkD -> "D" ; MkE -> "E" ; MkF -> "F" }
