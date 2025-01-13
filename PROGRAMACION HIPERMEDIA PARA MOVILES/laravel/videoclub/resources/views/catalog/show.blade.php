@extends('layouts.master')

@section('content')
<div class="row">
    <div class="col-sm-4">
        <img src="{{ $pelicula['poster'] }}" style="width:100%">
    </div>
    <div class="col-sm-8">
        <h2>{{ $pelicula['title'] }}</h2>
        <h4>Año: {{ $pelicula['year'] }}</h4>
        <h4>Director: {{ $pelicula['director'] }}</h4>
        <p><strong>Resumen:</strong> {{ $pelicula['synopsis'] }}</p>

        @if ($pelicula['rented'])
            <p><strong>Estado:</strong> Película actualmente alquilada</p>
            <button class="btn btn-danger">Devolver película</button>
        @else
            <p><strong>Estado:</strong> Película disponible</p>
            <button class="btn btn-primary">Alquilar película</button>
        @endif

        <a href="{{ url('/catalog/edit/' . $id) }}" class="btn btn-warning">Editar película</a>
        <a href="{{ url('/catalog') }}" class="btn btn-secondary">Volver al listado</a>
    </div>
</div>
@endsection
