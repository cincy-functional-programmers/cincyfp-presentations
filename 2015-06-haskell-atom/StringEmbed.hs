-- Courtesy of:
-- http://techoverflow.net/blog/2014/07/28/using-quasiquotation-for-more-readable-atom-code/
-- Who gives credit to: https://www.fpcomplete.com/user/marcin/template-haskell-101
module StringEmbed(embedStr, embedStrFile) where
 
import Language.Haskell.TH
import Language.Haskell.TH.Quote

embedStr :: QuasiQuoter 
embedStr = QuasiQuoter { quoteExp = stringE,
                    quotePat = undefined,
                    quoteDec = undefined,
                    quoteType = undefined }

embedStrFile :: QuasiQuoter
embedStrFile = quoteFile embedStr
