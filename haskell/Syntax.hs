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

-- Pretty-print an instruction beginning at the given indentation level
pShowInstruction :: Int -> Instruction -> String
pShowInstruction i inst = case inst of
    Compose inst1 inst2     ->
        pShowInstruction i inst1 ++ pShowInstruction i inst2
    If cnd sdir inst1 inst2 ->
        tabs i ++ "if " ++ show cnd ++ ' ' : show sdir ++ " do\n" 
            ++ pShowInstruction (i + 1) inst1
            ++ tabs i ++ "else\n"
            ++ pShowInstruction (i + 1) inst2
            ++ tabs i ++ "end\n"
    Mark mk                 -> tabs i ++ "mark " ++ show mk ++ "\n"
    Unmark mk               -> tabs i ++ "unmark " ++ show mk ++ "\n"
    PickUp                  -> tabs i ++ "pickup\n"
    PickUpElse inst         ->
        tabs i ++ "pickupelse\n"
            ++ pShowInstruction (i + 1) inst
            ++ tabs i ++ "end\n"
    Drop                    -> tabs i ++ "drop\n"
    Turn lr                 -> tabs i ++ "turn " ++ show lr ++ "\n"
    Move                    -> tabs i ++ "move\n"
    MoveElse inst           ->
        tabs i ++ "moveelse\n"
            ++ pShowInstruction (i + 1) inst
            ++ tabs i ++ "end\n"
    Flip prob inst1 inst2   ->
        tabs i ++ "flip " ++ show prob ++ " do\n"
            ++ pShowInstruction (i + 1) inst1
            ++ tabs i ++ "else\n"
            ++ pShowInstruction (i + 1) inst2
            ++ tabs i ++ "end\n"
    Pass                    -> tabs i ++ "pass\n"

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
    show cd = case cd of
        Friend     -> "friend"
        FriendFood -> "friendfood"
        Foe        -> "foe"
        FoeFood    -> "foefood"
        Food       -> "food"
        Home       -> "home"
        FoeHome    -> "foehome"
        Rock       -> "rock"
        Marker mk  -> "marker " ++ show mk

data LR = ALeft | ARight deriving (Eq, Ord)
instance Show LR where
    show lr = case lr of { ALeft -> "left" ; ARight -> "right" }

data MkType = MkA | MkB | MkC | MkD | MkE | MkF deriving (Eq, Ord)
instance Show MkType where
    show mk = case mk of
        { MkA -> "a" ; MkB -> "b" ; MkC -> "c"
        ; MkD -> "d" ; MkE -> "e" ; MkF -> "f"
        }
