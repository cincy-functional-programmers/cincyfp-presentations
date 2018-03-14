module FunctionMonad where

-- instance Applicative ((->) a) where
--   pure b = \a -> b
--   ff <*> fa = \a -> (ff a) (fa a)
--
-- instance Monad ((->) a) where
--   return = pure
--   ma >>= f = \a -> (f (ma a)) a
--
-- instance Functor ((->) a) where
--   fmap f fa = fa >>= (\a -> return $ f a)
