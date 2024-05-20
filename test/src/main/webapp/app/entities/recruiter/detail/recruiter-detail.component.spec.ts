import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { RecruiterDetailComponent } from './recruiter-detail.component';

describe('Recruiter Management Detail Component', () => {
  let comp: RecruiterDetailComponent;
  let fixture: ComponentFixture<RecruiterDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RecruiterDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RecruiterDetailComponent,
              resolve: { recruiter: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RecruiterDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RecruiterDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load recruiter on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RecruiterDetailComponent);

      // THEN
      expect(instance.recruiter()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
