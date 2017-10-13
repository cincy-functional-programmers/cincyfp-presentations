using System;

namespace Example4
{
    public static class ReaderExtensions
    {
        public static Reader<TState1, T> AndThen<TState1, TState2, T>(
            this Reader<TState1, TState2> reader,
            Reader<TState2, T> reader2)
        {
            return reader.Select(reader2.Run);
        }

        public static Reader<TState, T2> SelectMany<TState, T1, T2>(
            this Reader<TState, T1> reader, 
            Func<T1, Reader<TState, T2>> f)
        {
            return new Reader<TState, T2>(state => 
                f(reader.Run(state)).Run(state));
        }

        public static Reader<TState, T> Pure<TState, T>(this T t)
        {
            return new Reader<TState, T>(state => t);
        }

        public static Reader<TState, T2> Select<TState, T1, T2>(
            this Reader<TState, T1> reader,
            Func<T1, T2> f)
        {
            return reader.SelectMany(t1 => f(t1).Pure<TState, T2>());
        }

        public static Reader<TState, TSelect> SelectMany<TState, T1, T2, TSelect>(
            this Reader<TState, T1> reader,
            Func<T1, Reader<TState, T2>> f,
            Func<T1, T2, TSelect> selector)
        {
            return reader.SelectMany(t1 => f(t1).SelectMany(t2 => selector(t1, t2).Pure<TState, TSelect>()));
        }
    }
}
