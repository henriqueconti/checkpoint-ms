package br.com.checkpoint.microsservices.controller;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.checkpoint.microsservices.model.Cliente;
import br.com.checkpoint.microsservices.model.dto.ClienteDto;
import br.com.checkpoint.microsservices.model.dto.ClienteDtoLogin;
import br.com.checkpoint.microsservices.repository.ClienteRepository;


@Controller
public class ClienteController {
	
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ClienteRepository clienteRepository;
	
	@ModelAttribute("clienteDto")
    public ClienteDto clienteDto() {
        return new ClienteDto();
    }
	
	@ModelAttribute("clienteDtoLogin")
    public ClienteDtoLogin clienteDtoLogin() {
        return new ClienteDtoLogin();
    }
	
    @GetMapping("/login")
    public ModelAndView login() {
    	ModelAndView modelView = new ModelAndView("login");
		return modelView;
    }
    
    @GetMapping("/registro")
    public ModelAndView registro() {
    	ModelAndView modelView = new ModelAndView("registro");
		return modelView;
    }

	@PostMapping("/cliente")
	public ModelAndView cadastrarCliente(@ModelAttribute("clienteDto")  @Valid ClienteDto clienteDto, BindingResult bindingResult) 
	{
		if(bindingResult.hasErrors()) {
			ModelAndView modelView = new ModelAndView("registro");
			return modelView;
		}
		
		Cliente cliente = modelMapper.map(clienteDto, Cliente.class);
		
		Cliente cliente_consulta = clienteRepository.findByEmail(cliente.getEmail());
		
		if(cliente_consulta == null) {
			clienteRepository.save(cliente);
		}
		return new ModelAndView("redirect:/login");
	}
	
	
	@PostMapping("/cliente/logar")
	public ModelAndView logarCliente(@ModelAttribute("clienteDtoLogin")  @Valid ClienteDtoLogin clienteDtoLogin, BindingResult bindingResult) 
	{
		if(bindingResult.hasErrors()) {
			ModelAndView modelView = new ModelAndView("login");
			return modelView;
		}
		
		Cliente cliente = clienteRepository.findByEmail(clienteDtoLogin.getEmail());
		
		if(cliente != null) {
			ModelAndView modelView = new ModelAndView("index");
			return modelView;
		}
		
		return new ModelAndView("redirect:/login");
	}
}
