using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Readonly
{
    public class Turtle
    {
        public readonly string Name;
        public readonly Position Position;

        public Turtle(string name, Position position)
        {
            Name = name;
            Position = position;
        }

        public override string ToString()
        {
            return $"Turtle(Name={Name}, Position={Position})";
        }
    }

    public class Position
    {
        public readonly int X;
        public readonly int Y;

        public Position(int x, int y)
        {
            X = x;
            Y = y;
        }

        public override string ToString()
        {
            return $"Position(X={X}, Y={Y})";
        }
    }
}
