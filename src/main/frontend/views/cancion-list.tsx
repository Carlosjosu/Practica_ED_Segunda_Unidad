import { useEffect, useState } from 'react';
import { ViewConfig } from '@vaadin/hilla-file-router/types.js';
import { Button, Grid, GridColumn, TextField, VerticalLayout, Dialog, GridSortColumn, Checkbox, ComboBox, HorizontalLayout, Select, Icon } from '@vaadin/react-components';
import { Notification } from '@vaadin/react-components/Notification';
import { CancionServices, GeneroService, AlbumService } from 'Frontend/generated/endpoints';
import { useSignal } from '@vaadin/hilla-react-signals';
import handleError from 'Frontend/views/_ErrorHandler';
import { Group, ViewToolbar } from 'Frontend/components/ViewToolbar';
import type { GridItemModel } from '@vaadin/react-components';
import Genero from 'Frontend/generated/com/unl/estrdts/base/models/Genero';
import Album from 'Frontend/generated/com/unl/estrdts/base/models/Album';


export const config: ViewConfig = {
  title: 'Cancion',
  menu: {
    icon: 'vaadin:music',
    order: 3,
    title: 'Cancion',
  },
};

type CancionEntryFormProps = {
  onCancionCreated?: () => void;
  albumes: Album[];
  generos: Genero[];
  tipos: string[];
};

function CancionEntryForm(props: CancionEntryFormProps) {
  const dialogOpened = useSignal(false);
  const nombre = useSignal('');
  const duracion = useSignal('');
  const url = useSignal('');
  const tipo = useSignal('');
  const genero = useSignal('');
  const album = useSignal('');


  const generosCombo = props.generos.map(g => ({
    value: g.id?.toString() ?? '',
    label: g.nombre ?? ''
  }));
  const albumesCombo = props.albumes.map(a => ({
    value: a.id?.toString() ?? '',
    label: a.nombre ?? ''
  }));

  const createCancion = async () => {
    try {
      if (
        nombre.value.trim().length > 0 &&
        duracion.value.trim().length > 0 &&
        url.value.trim().length > 0 &&
        tipo.value.trim().length > 0 &&
        genero.value &&
        album.value
      ) {
        await CancionServices.create(
          nombre.value,
          parseInt(genero.value),
          parseInt(duracion.value),
          url.value,
          tipo.value,
          parseInt(album.value)
        );
        if (props.onCancionCreated) props.onCancionCreated();
        nombre.value = '';
        duracion.value = '';
        url.value = '';
        tipo.value = '';
        genero.value = '';
        album.value = '';
        dialogOpened.value = false;
        Notification.show('Canción creada exitosamente', { duration: 5000, position: 'bottom-end', theme: 'success' });
      } else {
        Notification.show('No se pudo crear, faltan datos', { duration: 5000, position: 'top-center', theme: 'error' });
      }
    } catch (error) {
      handleError(error);
    }
  };

  return (
    <>
      <Dialog
        modeless
        headerTitle="Registrar Canción"
        opened={dialogOpened.value}
        onOpenedChanged={({ detail }) => { dialogOpened.value = detail.value; }}
        footer={
          <>
            <Button onClick={() => (dialogOpened.value = false)}>Cancelar</Button>
            <Button className="btn-registrar" onClick={createCancion} theme="primary">Registrar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre" value={nombre.value} onValueChanged={evt => (nombre.value = evt.detail.value)} />
          <TextField label="Duración" type="number" value={duracion.value} onValueChanged={evt => (duracion.value = evt.detail.value)} />
          <TextField label="Url" value={url.value} onValueChanged={evt => (url.value = evt.detail.value)} />
          <ComboBox
            label="Género"
            items={generosCombo}
            itemLabelPath="label"
            itemValuePath="value"
            value={genero.value}
            onValueChanged={e => (genero.value = e.detail.value)}
            placeholder="Seleccione género"
          />
          <ComboBox
            label="Tipo de archivo"
            items={props.tipos.map(t => ({ value: t, label: t }))}
            itemLabelPath="label"
            itemValuePath="value"
            value={tipo.value}
            onValueChanged={e => (tipo.value = e.detail.value)}
            placeholder="Seleccione tipo de archivo"
          />
          <ComboBox
            label="Álbum"
            items={albumesCombo}
            itemLabelPath="label"
            itemValuePath="value"
            value={album.value}
            onValueChanged={e => (album.value = e.detail.value)}
            placeholder="Seleccione álbum"
          />
        </VerticalLayout>
      </Dialog>
      <Button className="btn-registrar" onClick={() => (dialogOpened.value = true)}>Registrar</Button>
    </>
  );
}

export default function CancionListView() {
  const [items, setItems] = useState<any[]>([]);
  const [albumes, setAlbumes] = useState<Album[]>([]);
  const [generos, setGeneros] = useState<Genero[]>([]);
  const [tipos, setTipos] = useState<string[]>([]);
  const [generoMap, setGeneroMap] = useState<Record<number, string>>({});
  const [albumMap, setAlbumMap] = useState<Record<number, string>>({});
  const [criterio, setCriterio] = useState<string>('nombre');
  const [texto, setTexto] = useState<string>('');
  const [editDialogOpened, setEditDialogOpened] = useState(false);
  const [cancionEdit, setCancionEdit] = useState<any | null>(null);
  const itemSelect = [
    { label: 'Cancion', value: 'nombre' },
    { label: 'Género', value: 'idGenero' },
    { label: 'Álbum', value: 'idAlbum' },
    { label: 'Tipo de archivo', value: 'tipo' } // Agregado para búsqueda por tipo
  ];

  const callData = () => {
    CancionServices.listCancion().then(function(data){
      setItems(data ?? []);
    });
  };

  useEffect(() => {
    callData();
    GeneroService.listAllGenero()
      .then((result) => {
        const generos = (result ?? []).filter((g): g is Genero => !!g && typeof g === 'object');
        const map: Record<number, string> = {};
        generos.forEach(g => {
          if (g.id !== undefined) {
            map[g.id] = (g.nombre !== undefined ? g.nombre : (g as any).nombreg) ?? '';
          }
        });
        setGeneroMap(map);
        setGeneros(generos);
      })
      .catch(console.error);

    AlbumService.listAllAlbum()
      .then((result) => {
        const albumes = (result ?? []).filter((a): a is Album => !!a && typeof a === 'object');
        const map: Record<number, string> = {};
        albumes.forEach(a => {
        if (a.id !== undefined) {
          map[a.id] = a.nombre ?? '';
        }
      });
        setAlbumMap(map);
        setAlbumes(albumes);
      })
      .catch(console.error);

    CancionServices.listTipo()
      .then((tipos) => setTipos((tipos ?? []).filter((t): t is string => !!t)))
      .catch(console.error);
  }, []);

  const order = (event: any, columnId: string) => {
    const direction = event.detail.value;
    const dir = direction === 'asc' ? 1 : 2;
    CancionServices.ordenar(columnId, dir).then(function (data) {
      setItems(data ?? []);
    });
  };

  function indexIndex({ model }: { model: GridItemModel<any> }) {
    return <span>{model.index + 1}</span>;
  }

  function normalizarTexto(texto: string) {
    return texto
      .normalize('NFD')
      .replace(/\p{Diacritic}/gu, '')
      .toLowerCase()
      .trim();
  }

  const search = async () => {
    try {
      let results: any[] = [];
      let searchValue = texto;
      if (criterio === 'idGenero') {
        const textoNorm = normalizarTexto(texto);
        const generoEncontrado = generos.find(g => normalizarTexto(g.nombre ?? '').includes(textoNorm));
        if (generoEncontrado && generoEncontrado.id !== undefined) {
          searchValue = generoEncontrado.id.toString();
        } else {
          setItems([]);
          Notification.show('No se encontró el género', { duration: 3000, position: 'top-center', theme: 'error' });
          return;
        }
      } else if (criterio === 'idAlbum') {
        const textoNorm = normalizarTexto(texto);
        const albumEncontrado = albumes.find(a => normalizarTexto(a.nombre ?? '').includes(textoNorm));
        if (albumEncontrado && albumEncontrado.id !== undefined) {
          searchValue = albumEncontrado.id.toString();
        } else {
          setItems([]);
          Notification.show('No se encontró el álbum', { duration: 3000, position: 'top-center', theme: 'error' });
          return;
        }
      }
      results = await CancionServices.search(criterio, searchValue, 1);
      if (results && results.length > 0) {
        setItems(results);
        Notification.show('Canción encontrada', { duration: 3000, position: 'top-center', theme: 'success' });
      } else {
        setItems([]);
        Notification.show('No se encontró ninguna coincidencia', { duration: 3000, position: 'top-center', theme: 'error' });
      }
    } catch (e) {
      Notification.show('Error en la búsqueda', { duration: 3000, position: 'top-center', theme: 'error' });
      console.error(e);
    }
  };

  const handleEdit = (item: any) => {
    setCancionEdit(item);
    setEditDialogOpened(true);
  };
  const handleEditSave = async () => {
    if (!cancionEdit) return;
    try {
      await CancionServices.update(
        parseInt(cancionEdit.id),
        cancionEdit.nombre,
        parseInt(cancionEdit.idGenero),
        parseInt(cancionEdit.duracion),
        cancionEdit.url,
        cancionEdit.tipo,
        parseInt(cancionEdit.idAlbum)
      );
      setEditDialogOpened(false);
      setCancionEdit(null);
      callData();
      Notification.show('Canción actualizada', { duration: 3000, position: 'top-center', theme: 'success' });
    } catch (e) {
      Notification.show('Error al actualizar', { duration: 3000, position: 'top-center', theme: 'error' });
    }
  };

  return (
    <main className="w-full h-full flex flex-col box-border gap-s p-m">
      <ViewToolbar title="Canciones">
        <Group>
          <CancionEntryForm onCancionCreated={callData} albumes={albumes} generos={generos} tipos={tipos} />
        </Group>
        <HorizontalLayout theme="spacing" style={{ alignItems: 'baseline' }}>
          <Select items={itemSelect}
            value={criterio}
            onValueChanged={(evt) => setCriterio(evt.detail.value)}
            placeholder="Seleccione un criterio"
          />
          <TextField
            placeholder="Search"
            style={{ width: '50%' }}
            value={texto}
            onValueChanged={(evt) => setTexto(evt.detail.value)}
          >
            <Icon slot="prefix" icon="vaadin:search" />
          </TextField>
          <Button onClick={search} className="btn-buscar" theme="primary" style={{ marginRight: '0.5rem' }}>
            BUSCAR
          </Button>
        </HorizontalLayout>
      </ViewToolbar>
      <Grid items={items}>
        <GridColumn renderer={indexIndex} header="Nro" />
        <GridSortColumn path="nombre" header="Canción" onDirectionChanged={e => order(e, 'nombre')} />
        <GridSortColumn header="Género" path="idGenero" onDirectionChanged={e => order(e, 'idGenero')} renderer={({ item }) => generoMap[item.idGenero] || 'Sin género'} />
        <GridSortColumn header="Álbum" path="idAlbum" onDirectionChanged={e => order(e, 'idAlbum')} renderer={({ item }) => albumMap[item.idAlbum] || 'Sin álbum'} />
        <GridSortColumn path="tipo" header="Tipo de archivo" onDirectionChanged={e => order(e, 'tipo')} />
        <GridSortColumn path="duracion" header="Duración" onDirectionChanged={e => order(e, 'duracion')} />
        <GridColumn header="Acciones" renderer={({ item }) => (
          <HorizontalLayout theme="spacing" style={{ alignItems: 'baseline' }}>
            <Button theme="primary" className="btn-editar" onClick={() => handleEdit(item)} style={{ marginRight: '0.5rem' }}>
              Editar
            </Button>
            <Button theme="error" className="btn-eliminar" onClick={async () => {
              if (!window.confirm('¿Seguro que desea eliminar esta canción?')) return;
              try {
                await CancionServices.delete(item.id);
                Notification.show('Canción eliminada', { duration: 3000, position: 'top-center', theme: 'success' });
                callData();
              } catch (e) {
                Notification.show('Error al eliminar', { duration: 3000, position: 'top-center', theme: 'error' });
              }
            }}>
              Eliminar
            </Button>
          </HorizontalLayout>
        )} />
      </Grid>
      <Dialog
        headerTitle="Editar Canción"
        opened={editDialogOpened}
        onOpenedChanged={({ detail }) => setEditDialogOpened(detail.value)}
        footer={
          <>
            <Button onClick={() => setEditDialogOpened(false)}>Cancelar</Button>
            <Button theme="primary" onClick={handleEditSave}>Guardar</Button>
          </>
        }
      >
        <VerticalLayout style={{ alignItems: 'stretch', width: '18rem', maxWidth: '100%' }}>
          <TextField label="Nombre" value={cancionEdit?.nombre ?? ''} onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, nombre: e.detail.value }))} /> 
          <TextField label="Duración" type="number" value={cancionEdit?.duracion?.toString() ?? ''} onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, duracion: e.detail.value }))} />
          <TextField label="Url" value={cancionEdit?.url ?? ''} onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, url: e.detail.value }))} />
          <ComboBox
            label="Género"
            items={generos.map(g => ({ value: g.id?.toString() ?? '', label: g.nombre ?? '' }))}
            itemLabelPath="label"
            itemValuePath="value"
            value={cancionEdit?.idGenero?.toString() ?? ''}
            onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, idGenero: e.detail.value }))}
            placeholder="Seleccione género"
          />
          <ComboBox
            label="Tipo de archivo"
            items={tipos.map(t => ({ value: t, label: t }))}
            itemLabelPath="label"
            itemValuePath="value"
            value={cancionEdit?.tipo ?? ''}
            onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, tipo: e.detail.value }))}
            placeholder="Seleccione tipo de archivo"
          />
          <ComboBox
            label="Álbum"
            items={albumes.map(a => ({ value: a.id?.toString() ?? '', label: a.nombre ?? '' }))}
            itemLabelPath="label"
            itemValuePath="value"
            value={cancionEdit?.idAlbum?.toString() ?? ''}
            onValueChanged={e => setCancionEdit((prev: any) => ({ ...prev, idAlbum: e.detail.value }))}
            placeholder="Seleccione álbum"
          />
        </VerticalLayout>
      </Dialog>
    </main>
  );
}
