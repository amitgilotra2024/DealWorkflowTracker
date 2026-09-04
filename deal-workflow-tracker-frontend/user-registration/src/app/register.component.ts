import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class AppComponent {
  title = 'user-registration';
}
