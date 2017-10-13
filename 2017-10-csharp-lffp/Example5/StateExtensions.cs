using System;

namespace Example5
{
    public static class StateExtensions
    {
        public static State<TState, TValue> Pure<TState, TValue>(this TValue value)
        {
            return new State<TState, TValue>(s => Tuple.Create(s, value));
        }

        public static State<TState, TValue> Get<TState, TValue>(this Func<TState, TValue> f)
        {
            return new State<TState, TValue>(s => Tuple.Create(s, f(s)));
        }

        public static State<TState, Unit> Put<TState>(this TState newState)
        {
            return new State<TState, Unit>(s => Tuple.Create(s, Unit.Only));
        }

        public static State<TState, Unit> Mod<TState>(Func<TState, TState> f)
        {
            return new State<TState, Unit>(s => Tuple.Create(f(s), Unit.Only));
        }

        public static State<TState, T2> SelectMany<TState, T1, T2>(this State<TState, T1> state,
            Func<T1, State<TState, T2>> f)
        {
            return new State<TState, T2>(s =>
            {
                var r1 = state.Run(s);
                var s2 = f(r1.Item2);
                return s2.Run(r1.Item1);
            });
        }

        public static State<TState, T2> Select<TState, T1, T2>(this State<TState, T1> state, Func<T1, T2> f)
        {
            return state.SelectMany(t1 => f(t1).Pure<TState, T2>());
        }

        public static State<TState, TSelect> SelectMany<TState, T1, T2, TSelect>(
            this State<TState, T1> state,
            Func<T1, State<TState, T2>> f,
            Func<T1, T2, TSelect> selector)
        {
            return state.SelectMany(t1 => f(t1).SelectMany(t2 => selector(t1, t2).Pure<TState, TSelect>()));
        } 
    }
}
