using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace LFFP
{
    public class Turtle
    {
        public string Name { get; set; }
        public Position Position { get; set; }

        public override string ToString()
        {
            return $"Turtle(Name={Name}, Position={Position})";
        }
    }

    public class Position
    {
        public int X { get; set; }
        public int Y { get; set; }

        public override string ToString()
        {
            return $"Position(X={X}, Y={Y})";
        }
    }
}
