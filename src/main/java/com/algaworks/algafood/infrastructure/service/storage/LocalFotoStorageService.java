package com.algaworks.algafood.infrastructure.service.storage;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;

import com.algaworks.algafood.core.storage.StorageProperties;
import com.algaworks.algafood.domain.service.FotoStorageService;

public class LocalFotoStorageService implements FotoStorageService {

	@Autowired
	private StorageProperties storageProperties;

	@Override
	public void armazenar(NovaFoto novaFoto) {

		try {
			Path arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());

			FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
		} catch (Exception e) {
			throw new StorageException("Não foi possivel armazenar arquivo. ", e);
		}
	}

	private Path getArquivoPath(String nomeArquivo) {
		return storageProperties.getLocal().getDiretorioFotos().resolve(Path.of(nomeArquivo));

	}

	@Override
	public void remover(String fotoArquivo) {

		try {

			Path arquivoPath = getArquivoPath(fotoArquivo);
			Files.deleteIfExists(arquivoPath);
		} catch (Exception e) {
			throw new StorageException("Não foi possivel excluir arquivo. ", e);
		}

	}

	@Override
	public FotoRecuperada recuperar(String nomeArquivo) {
		Path arquivoPath = getArquivoPath(nomeArquivo);
		try {

			FotoRecuperada fotoRecuperada = FotoRecuperada.builder().inputStream(Files.newInputStream(arquivoPath))
					.build();

			return fotoRecuperada;
		} catch (Exception e) {
			throw new StorageException("Não foi possivel recuperar o arquivo. ", e);
		}
	}

}
